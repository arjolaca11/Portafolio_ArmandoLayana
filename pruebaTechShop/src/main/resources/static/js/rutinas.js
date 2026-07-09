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

//Para quitar toast
setTimeout(() => {
    document.querySelectorAll('.toast').forEach(t => t.classList.remove('show'));
}, 4000);
