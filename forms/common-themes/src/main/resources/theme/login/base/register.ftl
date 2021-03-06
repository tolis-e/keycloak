<#import "template.ftl" as layout>
<@layout.registrationLayout bodyClass="register" ; section>

    <#if section = "title">

    ${rb.registerWith} ${realm.name}

    <#elseif section = "header">

    ${rb.registerWith} <strong>${realm.name}</strong>

    <#elseif section = "form">

    <form action="${url.registrationAction}" method="post">
        <p class="subtitle">${rb.allRequired}</p>
        <div>
            <label for="firstName">${rb.firstName}</label><input type="text" id="firstName" name="firstName" value="${register.formData.firstName!''}" />
        </div>
        <div>
            <label for="lastName">${rb.lastName}</label><input type="text" id="lastName" name="lastName" value="${register.formData.lastName!''}" />
        </div>
        <div>
            <label for="email">${rb.email}</label><input type="text" id="email" name="email" value="${register.formData.email!''}" />
        </div>
        <div>
            <label for="username">${rb.username}</label><input type="text" id="username" name="username" value="${register.formData.username!''}" />
        </div>
        <div>
            <label for="password">${rb.password}</label><input type="password" id="password" name="password" />
        </div>
        <div>
            <label for="password-confirm">${rb.passwordConfirm}</label><input type="password" id="password-confirm" name="password-confirm" />
        </div>

        <input class="btn-primary" type="submit" value="Register"/>
    </form>

    <#elseif section = "info">

    <p>${rb.alreadyHaveAccount} <a href="${url.loginUrl}">${rb.logIn}</a>.</p>

    </#if>

</@layout.registrationLayout>