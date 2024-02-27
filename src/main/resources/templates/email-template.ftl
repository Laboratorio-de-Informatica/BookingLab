<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Confirmación de Reserva</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .container {
            max-width: 600px;
            margin: auto;
        }
        .header {
            background-color: #4CAF50;
            color: white;
            padding: 10px;
            text-align: center;
        }
        .content {
            padding: 20px;
            border: 1px solid #ddd;
        }
        .footer {
            background-color: #4CAF50;
            color: white;
            text-align: center;
            padding: 10px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h2>Confirmaci&oacute;n de Reserva</h2>
    </div>
    <div class="content">
        <p>Estimado/a ${teacher},</p>
        <p>Se ha realizado con &eacute;xito una reserva para el laboratorio de inform&aacute;tica.</p>
        <p>&#128240;Detalles de la reserva:</p>
        <ul>
            <li><strong>&#128210; Curso:</strong> ${course}</li>
            <li><strong>&#128205; Laboratorio:</strong> ${laboratory}</li>
            <li><strong>&#128197; Fecha:</strong> ${date}</li>
            <li><strong>&#128197; D&iacute;a:</strong> ${day}</li>
            <li><strong>&#128336; Hora Inicial:</strong> ${initialTimeSlot}</li>
            <li><strong>&#128343; Hora Final:</strong> ${finalTimeSlot}</li>
        </ul>
        <p>Agradecemos su colaboraci&oacute;n y esperamos que su tiempo en el laboratorio sea productivo &#128512;.</p>
        <p>Atentamente,</p>
        <p>Laboratorio de Inform&aacute;tica</p>
    </div>
    <div class="footer">
        <p>Este es un correo autogenerado. Por favor, no responda el correo.</p>
    </div>
</div>
</body>
</html>
