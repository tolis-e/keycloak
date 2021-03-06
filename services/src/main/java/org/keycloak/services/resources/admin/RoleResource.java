package org.keycloak.services.resources.admin;

import org.keycloak.models.ApplicationModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.services.managers.ModelToRepresentation;

import javax.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class RoleResource {
    protected RealmModel realm;

    public RoleResource(RealmModel realm) {
        this.realm = realm;
    }

    protected RoleRepresentation getRole(RoleModel roleModel) {
        return ModelToRepresentation.toRepresentation(roleModel);
    }

    protected void deleteRole(RoleModel role) {
        if (!role.getContainer().removeRoleById(role.getId())) {
            throw new NotFoundException();
        }
    }

    protected void updateRole(RoleRepresentation rep, RoleModel role) {
        role.setName(rep.getName());
        role.setDescription(rep.getDescription());
        role.setComposite(rep.isComposite());
    }

    protected void addComposites(List<RoleRepresentation> roles, RoleModel role) {
        for (RoleRepresentation rep : roles) {
            RoleModel composite = realm.getRoleById(rep.getId());
            if (composite == null) {
                throw new NotFoundException("Could not find composite role: " + rep.getName());
            }
            if (!role.isComposite()) role.setComposite(true);
            role.addCompositeRole(composite);
        }
    }

    protected Set<RoleRepresentation> getRoleComposites(RoleModel role) {
        if (!role.isComposite() || role.getComposites().size() == 0) return Collections.emptySet();

        Set<RoleRepresentation> composites = new HashSet<RoleRepresentation>(role.getComposites().size());
        for (RoleModel composite : role.getComposites()) {
            composites.add(ModelToRepresentation.toRepresentation(composite));
        }
        return composites;
    }

    protected Set<RoleRepresentation> getRealmRoleComposites(RoleModel role) {
        if (!role.isComposite() || role.getComposites().size() == 0) return Collections.emptySet();

        Set<RoleRepresentation> composites = new HashSet<RoleRepresentation>(role.getComposites().size());
        for (RoleModel composite : role.getComposites()) {
            if (composite.getContainer() instanceof RealmModel)
                composites.add(ModelToRepresentation.toRepresentation(composite));
        }
        return composites;
    }

    protected Set<RoleRepresentation> getApplicationRoleComposites(String appName, RoleModel role) {
        if (!role.isComposite() || role.getComposites().size() == 0) return Collections.emptySet();

        ApplicationModel app = realm.getApplicationByName(appName);
        if (app == null) {
            throw new NotFoundException("Could not find application: " + appName);

        }

        Set<RoleRepresentation> composites = new HashSet<RoleRepresentation>(role.getComposites().size());
        for (RoleModel composite : role.getComposites()) {
            if (composite.getContainer().equals(app))
                composites.add(ModelToRepresentation.toRepresentation(composite));
        }
        return composites;
    }

    protected void deleteComposites(List<RoleRepresentation> roles, RoleModel role) {
        for (RoleRepresentation rep : roles) {
            RoleModel composite = realm.getRoleById(rep.getId());
            if (composite == null) {
                throw new NotFoundException("Could not find composite role: " + rep.getName());
            }
            role.removeCompositeRole(composite);
        }
        if (role.getComposites().size() == 0) role.setComposite(false);
    }
}
