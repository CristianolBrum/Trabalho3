$(function () {
    exibir($('#exibir-clientes'));
    $("#form-cliente").submit(function (e) {
        e.preventDefault();
        inserir($(this));
    });
    $('#exibir-clientes').click(function() {
        exibir($(this));
    });
    $('#form-buscar').submit(function(e) {
        e.preventDefault();
        exibir();
    });
});

function inserir(form) {
    $.ajax({
        url: 'Inserir',
        type: 'post',
        dataType: 'json',
        data: form.serialize(),
        beforeSend: function () {
            $('input, button', form).attr('disabled', true);
        },
        success: function (json) {
            if(!json.sucesso) {
                alert("Ocorreu um erro ao cadastrar o cliente.");
                return;
            }
            exibir($('#exibir-clientes'));
            
            $('input, button', form).attr('disabled', false);
            $('input').val('');
            alert('Cliente cadastrado com sucesso.');
            $(json.cidades).each(function (indice, cidade) {
                $('#lista-cidades').append('<p>' + cidade.codigo + ' - ' + cidade.nome + '</p>');
            });
        },
        error: function () {
            $('input, button, select', form).attr('disabled', false);
            alert('Ocorreu um erro ao cadastrar o cliente.');
        }
    });
}

function exibir(btn) {
    var form = $('#form-buscar');
    $.ajax({
        url: 'Exibir',
        type: 'post',
        dataType: 'json',
        data: btn ? {buscar : ''} : form.serialize(),
        beforeSend: function() {
            if(btn) btn.button('loading');
            $('input', form).attr('disabled', true);
            $('button', form).button('loading');
        },
        success: function (json) {
            if(btn) btn.button('reset');
            $('#lista-clientes tbody tr').remove();
            $('input', form).attr('disabled', false);
            $('button', form).button('reset');
            $(json.clientes).each(function (indice, cliente) {
                $('#lista-clientes tbody').append("<tr><td>"+cliente.nome+"</td><td>"+cliente.cpf+"</td><td>"+cliente.rg+"</td><td>"+cliente.telefone+"</td><td class='text-center'><button class='btn btn-danger' onclick='excluir("+cliente.id+")' data-id='"+cliente.id+"'>Excluir</button></td></tr>");
            });
        },
        error: function () {
            if(btn) btn.button('reset');
            $('input', form).attr('disabled', true);
            $('button', form).button('loading');
            alert("Erro ao exibir clientes");
        }
        
    });
}

function excluir(id) {
    if(!confirm('Tem certeza que deseja realmente excluir este cliente?'))
        return;
    
    var botao = $('button[data-id='+id+']').button('loading');
    $.ajax({
        url: 'Excluir',
        type: 'post',
        dataType: 'json',
        data: {id: id},
        beforeSend: function() {
            botao.button('loading');
        },
        success: function () {
            botao.parents('tr').remove();
            botao.button('reset');
        },
        error: function () {
            botao.button('reset');
            alert("Erro ao excluir cliente");
        }
        
    });
}