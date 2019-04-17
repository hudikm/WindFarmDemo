<#-- @ftlvariable name="" type="sk.fri.uniza.views.LoginPageView" -->
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
    <title>WindFarm Demo</title>
    <link rel="shortcut icon" type="image/png" href="${getRootPath()}img/favicon.png"/>

    <!-- CSS  -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="${getRootPath()}css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="${getRootPath()}css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>

<body>
<header>
    <nav class="light-blue lighten-1" role="navigation">
        <div class="nav-wrapper container">
            <a id="logo-container" href="#" class="brand-logo" style="height: 100%">
                <div class="valign-wrapper" style="height: 100%">
                    <img class="responsive-img" src="${getRootPath()}img/logo_small.png" style="width: 48px;margin-right: 5px">
                    <h4 class="white-text">WindFarm Demo</h4>
                </div>
            </a>
            <ul class="right hide-on-med-and-down">
                <li><a href="#">Navbar Link</a></li>
            </ul>

            <ul id="nav-mobile" class="sidenav">
                <li><a href="#">Navbar Link</a></li>
            </ul>
            <a href="#" data-target="nav-mobile" class="sidenav-trigger"><i class="material-icons">menu</i></a>
        </div>
    </nav>
</header>
<main>
    <div class="section no-pad-bot" id="index-banner">
        <div class="container">
            <br><br>
            <h1 class="header center orange-text">Vitajte v aplikácii WindFarm demo</h1>
            <div class="row">
                <div class="col s12 m8 offset-m2">
                    <div class="card">
                        <div class="card-content">
                            <span class="card-title">Prihlásenie:</span>
                            <div class="row">
                                <form class="col s12" action="${getAction()}" method="post">
                                    <div class="row">
                                        <div class="input-field col s12">
                                            <i class="material-icons prefix">account_circle</i>
                                            <input id="icon_prefix" type="text" name="username" class="validate">
                                            <label for="icon_prefix">Prihlasovacie meno</label>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="input-field col s12">
                                            <i class="material-icons prefix">vpn_key</i>
                                            <input id="vpn_key" type="password" name="password" class="validate">
                                            <label for="vpn_key">Heslo</label>
                                        </div>
                                    </div>
                                    <input type="hidden" name="sessionId" value="${getSessionId()}">

                                    <div class="row">
                                        <div class="input-field col s12">
                                            <button class="btn waves-effect waves-light orange" type="submit"
                                                    name="action">Prihlásiť
                                                <i class="material-icons right">send</i>
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <!--                    <div class="card-action">-->

                        <!--                    </div>-->
                    </div>
                </div>
            </div>

            <br><br>
        </div>
    </div>
</main>

<footer class="page-footer orange">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">WindFarm Demo</h5>
                <p class="grey-text text-lighten-4">Demo aplikácia demoštrujúca použitie <a class="white-text"
                                                                                            href="https://www.dropwizard.io/1.3.9/docs/">Dropwizard
                        <i class="tiny material-icons">link</i></a></p>

            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
            Made by <a class="orange-text text-lighten-3" href="http://materializecss.com">Materialize</a>
        </div>
    </div>
</footer>

<!--  Scripts-->
<script src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
<script src="${getRootPath()}js/materialize.js"></script>
<script src="${getRootPath()}js/init.js"></script>

</body>
</html>
