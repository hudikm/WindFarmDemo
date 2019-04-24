<#ftl encoding="utf-8">
<#-- @ftlvariable name="" type="sk.fri.uniza.views.LoginPageView" -->
<!DOCTYPE html>
<html lang="sk">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
    <title>WindFarm Demo</title>
    <link rel="shortcut icon" type="image/png" href="${getRootPath()}img/favicon.png"/>

    <!-- CSS  -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="/css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>

<body>
<main>
    <div class="section no-pad-bot" id="index-banner">
        <div class="container">
            <h4>Prihlásenie:</h4>
            <div class="row">
                <form class="col s12" action="${getAction()}" method="post">
                    <div class="row">
                        <div class="input-field col s12">
                            <i class="material-icons prefix">account_circle</i>
                            <input id="icon_prefix" type="text" name="username" class="validate"
                                   autocomplete="username email">
                            <label for="icon_prefix">Prihlasovacie meno</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12">
                            <i class="material-icons prefix">vpn_key</i>
                            <input id="vpn_key" type="password" name="password" class="validate"
                                   autocomplete="password">
                            <label for="vpn_key">Heslo</label>
                        </div>
                    </div>
                    <div class="row">
                        <div class="input-field col s12">
                            <label>
                                <input type="checkbox" name="stay_signin">
                                <span>Zostaň prihlásený</span>
                            </label>
                        </div>
                    </div>
                    <input type="hidden" name="sessionId" value="${getSessionId()}">

                    <div class="row">
                        <div class="input-field col s12">
                            <button class="btn waves-effect waves-light orange" type="submit"
                                    name="login" value="Login">Prihlásiť
                                <i class="material-icons right">send</i>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>

<!--  Scripts-->
<script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="/js/materialize.js"></script>
<script src="/js/init.js"></script>

</body>
</html>
