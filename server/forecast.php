<?php

// Si nos viene un POST con datos guardamos la información, si no mostramos el forecast
if(!empty($_POST['place']) && !empty($_POST['u'])) {
    $new_config = array(
        'place' => $_POST['place'],
        'tomorrow' => !empty($_POST['tomorrow']) ? TRUE : FALSE
    );

    file_put_contents(md5($_POST['u']) . '.txt', json_encode($new_config));
}

// Respondemos peticiones con un valor real
echo getForecastFromYahoo();

// Responder con peticiones falsas para ver cambios de colro más rápidos
// echo getForecastFromRandom();

/**
 * Hace una petición al servidor de Yahoo y devuelve un código de "forecast" numérico
 * @return string
 */
function getForecastFromYahoo() {
    // Configuración inicial
    $config = array(
        'place' => !empty($_GET['place']) ? $_GET['place'] : 'Madrid, ES',
        'tomorrow' => !empty($_GET['tomorrow']) ? TRUE : FALSE
    );

    // Intenta cargar una config desde storage
    if(!empty($_GET['u'])) {
        $user_config = file_get_contents(md5($_GET['u']) . '.txt');

        // Si no devuelve FALSE tenemos datos en el archivo
        if($user_config !== FALSE) {
            $user_config = json_decode($user_config, TRUE);

            // Y si los datos son un JSON válido, los cargamos en $config
            if($user_config !== NULL) {
                $config = $user_config;
            }
        }
    }

    $query  = 'select * from weather.forecast where woeid in (select woeid from geo.places(1) where text="' . $config['place'] .'")';
    $result = file_get_contents('http://query.yahooapis.com/v1/public/yql?q=' . urlencode($query) . '&u=c&format=json');

    $result = json_decode($result, TRUE);

    if($result['query'] && $result['query']['results']) {
        $results  = $result['query']['results']['channel'];
        $forecast = $results['item']['forecast'];

        $today    = $forecast[0];
        $tomorrow = $forecast[1];

        if($config['tomorrow'] === TRUE) {
            return '<forecast>' . getColorFromCode($tomorrow['code']) . '</forecast>';
        } else {
            return '<forecast>' . getColorFromCode($today['code']) . '</forecast>';
        }
    }

    // Si se retorna 10 entendemos que hay un error de configuración o servicio
    return '<forecast>10</forecast>';
}

/**
 * Devuelve un código de "forecast" aleatorio con un seed que cambia cada minuto
 */
function getForecastFromRandom() {
    srand(date('i'));
    return '<forecast>' . rand(1, 5) . '</forecast>';
}

/**
 * Devuelve el código de color a utilizar para un código dado
 *
 * @param  int $code
 * @return int
 */
function getColorFromCode($code) {
    // Caluroso
    $caluroso = array(36);

    // Soleado, agradable
    $soleado = array(31, 32, 33, 34);

    // Frío
    $frio = array(25);

    // Lluvia o similar
    $lluvioso = array(5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 36, 37, 38, 39, 40, 41, 42, 43, 45, 46, 47);

    // Nublado o similar
    $nublado = array(26, 27, 28, 29, 30, 44);

    // Devolver el código de color que corresponda
    if(in_array($code, $caluroso)) {
        return 1;
    }

    if(in_array($code, $soleado)) {
        return 2;
    }

    if(in_array($code, $frio)) {
        return 3;
    }

    if(in_array($code, $lluvioso)) {
        return 4;
    }

    if(in_array($code, $nublado)) {
        return 5;
    }

    // Si no se ha devuelto ninguno, dar un código alto para saber que no hay código
    return 10;
}

/**
 * Devuelve la descripción de las condiciones para un código según la tabla en
 * https://developer.yahoo.com/weather/documentation.html#codes
 *
 * @param  int $code
 * @return string
 */
function getConditionFromCode($code) {
    $codes = array(
        // '0'  => 'tornado',
        // '1'  => 'tropical storm',
        // '2'  => 'hurricane',
        // '3'  => 'severe thunderstorms',
        // '4'  => 'thunderstorms',

        '5'  => 'mixed rain and snow',
        '6'  => 'mixed rain and sleet',
        '7'  => 'mixed snow and sleet',
        '8'  => 'freezing drizzle',
        '9'  => 'drizzle',
        '10' => 'freezing rain',
        '11' => 'showers',
        '12' => 'showers',
        '13' => 'snow flurries',
        '14' => 'light snow showers',
        '15' => 'blowing snow',
        '16' => 'snow',
        '17' => 'hail',
        '18' => 'sleet',

        // '19' => 'dust',
        // '20' => 'foggy',
        // '21' => 'haze',
        // '22' => 'smoky',
        // '23' => 'blustery',
        // '24' => 'windy',

        '25' => 'cold',

        '26' => 'cloudy',
        '27' => 'mostly cloudy (night)',
        '28' => 'mostly cloudy (day)',
        '29' => 'partly cloudy (night)',
        '30' => 'partly cloudy (day)',

        '31' => 'clear (night)',
        '32' => 'sunny',
        '33' => 'fair (night)',
        '34' => 'fair (day)',

        '35' => 'mixed rain and hail',

        '36' => 'hot',

        '37' => 'isolated thunderstorms',
        '38' => 'scattered thunderstorms',
        '39' => 'scattered thunderstorms',
        '40' => 'scattered showers',
        '41' => 'heavy snow',
        '42' => 'scattered snow showers',
        '43' => 'heavy snow',

        '44' => 'partly cloudy',

        '45' => 'thundershowers',
        '46' => 'snow showers',
        '47' => 'isolated thundershowers',
    );

    if(array_key_exists($code, $codes)) {
        return $codes[$code];
    }

    return 'Not available';
}
