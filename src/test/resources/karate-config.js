// karate-config.js
// Este archivo se carga automáticamente antes de cada feature.
// Define configuración global: URL base, headers, variables de entorno.

function fn() {

  var config = {
    baseUrl:    'https://dummyjson.com',
    baseUrlWeb: 'https://www.mercadolibre.com.pe'
  };

  // Configurar headers por defecto para todas las llamadas
  karate.configure('headers', {
    'Content-Type': 'application/json',
    'Accept':       'application/json'
  });

  // Timeout de conexión: 10 segundos
  karate.configure('connectTimeout', 10000);
  karate.configure('readTimeout',    15000);

  return config;
}
