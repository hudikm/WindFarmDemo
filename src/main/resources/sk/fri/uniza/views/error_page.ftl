<#ftl encoding="utf-8">
<#-- @ftlvariable name="" type="sk.fri.uniza.views.ErrorView" -->
<!DOCTYPE html>
<html lang="sk">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0"/>
    <title>WindFarm Demo</title>
    <link rel="shortcut icon" type="image/png" href="/img/favicon.png"/>

    <!-- CSS  -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link href="/css/materialize.css" type="text/css" rel="stylesheet" media="screen,projection"/>
    <link href="/css/style.css" type="text/css" rel="stylesheet" media="screen,projection"/>
</head>

<body>
<main>
    <div class="container">
        <div class="valign-wrapper" style="margin-top: 5%">
            <div style="width: 100%;">
                <i class="large material-icons center-align" style="width: inherit;">
                    sentiment_very_dissatisfied
                </i>
                <h2 class="center-align" style="margin-top: 0px;">${getErrorMessage().code!}</h2>
                <h4 class="center-align">${(getErrorMessage().message!)?no_esc}</h4>
                <h6 class="center-align">${(getErrorMessage().details!)?no_esc}</h6>
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
