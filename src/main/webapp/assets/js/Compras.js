function prepararCalificacion(idVendedor, nombreVendedor, idTransaccion) {
    document.getElementById('calif_idVendedor').value = idVendedor;
    document.getElementById('calif_idTransaccion').value = idTransaccion;
    document.getElementById('calif_nombreVendedor').textContent = nombreVendedor;
}

function prepararReporte(idVendedor, nombreVendedor, idTransaccion) {
    document.getElementById('rep_idVendedor').value = idVendedor;
    document.getElementById('rep_idTransaccion').value = idTransaccion || '';
    document.getElementById('rep_nombreVendedor').textContent = nombreVendedor;
}