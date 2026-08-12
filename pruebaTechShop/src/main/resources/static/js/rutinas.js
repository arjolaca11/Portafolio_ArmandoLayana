// funcion para hacer un preview de una imagen
function mostrarImagen(input) {
    if (input.files && input.files[0]) {
        const imagen = input.files[0];
        // El limite viene de la tabla "constante" (atributo TAM_MAX_IMAGEN), inyectado
        // en cada pagina por GlobalModelAdvice; si no esta disponible se usan 512 Kb.
        const maximo = (typeof TAM_MAX_IMAGEN !== 'undefined' && TAM_MAX_IMAGEN) ? TAM_MAX_IMAGEN : 512 * 1024;
        if (imagen.size <= maximo) {
            var lector = new FileReader();
            lector.onload = function (e) {
                $('#blah').attr('src', e.target.result).height(200);
            };
            lector.readAsDataURL(input.files[0]);
        } else {
            alert("La imagen seleccionada es muy grande... no debe superar los " + Math.round(maximo / 1024) + " Kb!");
        }
    }
}

//Para insertar información en el modal según el registro...
document.addEventListener('DOMContentLoaded', function () {
    const confirmModal = document.getElementById('confirmModal');
    if (confirmModal) {
        confirmModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            document.getElementById('modId').value = button.getAttribute('data-bs-id');
            document.getElementById('modalDescripcion').textContent = button.getAttribute('data-bs-descripcion');
        });
    }
});

// Inyecta el token CSRF de Spring Security en todos los <form> de la pagina,
// para no tener que repetir el <input hidden> en cada formulario POST.
document.addEventListener('DOMContentLoaded', function () {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    const paramMeta = document.querySelector('meta[name="_csrf_parameter"]');
    if (!tokenMeta || !paramMeta) {
        return;
    }
    document.querySelectorAll('form').forEach(function (form) {
        if (form.querySelector('input[name="' + paramMeta.content + '"]')) {
            return;
        }
        const input = document.createElement('input');
        input.type = 'hidden';
        input.name = paramMeta.content;
        input.value = tokenMeta.content;
        form.appendChild(input);
    });
});

//Para quitar toast
setTimeout(() => {
    document.querySelectorAll('.toast').forEach(t => t.classList.remove('show'));
}, 4000);

/**
 * Agrega un producto al carrito asumiendo cantidad = 1.
 * @param {HTMLFormElement} formulario - El objeto form que contiene el ID del producto.
 */
function addCart(formulario) {
    // 1. Obtencion de datos y ruta (solo el ID del producto)
    var idProducto = $(formulario).find('input[name="idProducto"]').val();
    var ruta = $(formulario).attr('action') || '/carrito/agregar'; // Lee la ruta del atributo 'action'

    // 2. Seguridad (CSRF Token)
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");

    // 3. Peticion AJAX (solo se envia el ID del producto)
    $.ajax({
        url: ruta,
        type: 'POST',
        data: {
            // ** CRÍTICO: SOLO ENVIAMOS idProducto **
            idProducto: idProducto
        },
        beforeSend: function (xhr) {
            if (csrfHeader && csrfToken) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        },
        success: function (response) {
            // Actualiza el fragmento HTML del carrito
            $("#resultBlock").html(response);
            console.log("Producto agregado con cantidad por defecto (1).");
        },
        error: function (xhr, status, error) {
            // Manejo de errores
            var mensaje = xhr.responseText || 'Error en la conexión.';
            alert("Error al agregar producto: " + mensaje);
        }
    });
}
