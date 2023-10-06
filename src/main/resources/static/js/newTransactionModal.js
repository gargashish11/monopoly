let payerSelected = false;
let payer_id;
let payer_name;

let payeeSelected = false;
let payee_id;
let payee_name;

let amountSelected = false;
let transactionAmount = 0;
let newTransactionModalObject = null;

const addPlayersToTransactionModal = () => {
    newTransactionModalObject = bootstrap.Modal.getInstance($("#newTransactionModal"));
    $("#transactionAmount").val('');
    $("#addTransaction").prop('disabled', true);
    let selectTargets = [
        $("#newTransactionModal #payer"),
        $("#newTransactionModal #payee")
    ];

    $.each(selectTargets, function (i, selectTarget) {
        $(selectTarget).children().remove();
        $(selectTarget).append("<option selected>Choose...</option>");
        $.each(currentGame.gamePlayers, function (idx, gamePlayer) {
            $(selectTarget).append(playerOptionMarkUp(gamePlayer));
        })
    })
}

const playerOptionMarkUp = ({player_id, player_name}) => {
    return `
        <option value=${player_id}>${player_name}</option>
    `;
}

$(document).on('change', "#newTransactionModal #payer", function (event) {
    let selectedOption = $(event.currentTarget).find("option:selected");
    if (parseInt($(selectedOption).val())) {
        payerSelected = true;
        payer_id = parseInt($(selectedOption).val());
        payer_name = $(selectedOption).text();
    }
    toggleTransactionButton();
    checkTransactionParties();
})

$(document).on('change', "#newTransactionModal #payee", function (event) {
    let selectedOption = $(event.currentTarget).find("option:selected");
    if (parseInt($(selectedOption).val())) {
        payeeSelected = true;
        payee_id = parseInt($(selectedOption).val());
        payee_name = $(selectedOption).text();
    }
    toggleTransactionButton();
    checkTransactionParties();
})

$(document).on('change', "#transactionAmount", function (event) {
    transactionAmount = parseInt($(event.currentTarget).val());
    if (transactionAmount > 0 && transactionAmount < 5000) {
        amountSelected = true;
    }
    toggleTransactionButton();
})

const toggleTransactionButton = () => {
    if (payerSelected && payeeSelected && amountSelected) {
        $("#addTransaction").prop('disabled', false);
    }
}

const checkTransactionParties = () => {
    if (payer_id === payee_id) {
        $("#newTransactionModal #txnError").text("Payer and Payee are same.");
        return false;
    } else {
        $("#newTransactionModal #txnError").text("");
        return true;
    }
}

const addTransaction = () => {
    let transaction = {
        "game_id": currentGame.id,
        "payer_id": payer_id,
        "payer_name": payer_name,
        "payee_id": payee_id,
        "payee_name": payee_name,
        "amount": transactionAmount
    };
    if (checkTransactionParties()) {
        $.ajax({
            method: "PUT",
            url: `/transaction/add`,
            data: JSON.stringify(transaction),
            contentType: "application/json",
            dataType: "json",
            success: function (result) {
                if (result.txnSuccess === 1) {
                    newTransactionModalObject.hide();
                    getGameState(result.id);
                }
            }
        });
    }
}