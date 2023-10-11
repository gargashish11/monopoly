let players = [{}]
let newGameModalObject = null;

const newGameModal = () => {
    newGameModalObject = bootstrap.Modal.getInstance($("#newGameModal"));
    let appendTarget = $("#newGameModal .modal-body #playerForm");
    $(appendTarget).children().remove();
    $.get({
        url: "/player/all",
        success: function (result) {
            $.each(result, function (idx, player) {
                addPlayer(appendTarget, idx, player)
            })
        }
    })
}

const addPlayer = (appendTarget, idx, {id, name}) => {
    let playerMarkup = `
<div class="input-group playerData">
    <div class="text-success me-2 playerAdd">
        <i class="fa-solid fa-circle-plus"></i>
    </div>
    <input id="player[${idx}].id" class="hiddenId" type="hidden" value="${id}"/>
    <input id="player[${idx}].name" class="form-control" type="text" value="${name}"/>
    <div class="text-danger ms-2 playerRemove">
        <i class="fa-solid fa-circle-minus"></i>
    </div>
</div>`;
    $(appendTarget).append(playerMarkup);
}

const addPlayerHTML = (event) => {
    let playerCount = $(event.currentTarget).parents("#playerForm").children().length;
    addPlayer($(event.currentTarget).parents("#playerForm"), playerCount, {id: 0, name: ''})
}

const deletePlayer = (event) => {
    if ($("#playerForm").children().length > 3) {
        $("#newGameModal #playerError").text("");
        deletePlayerHTML($(event.currentTarget).parent());
    } else {
        $("#newGameModal #playerError").text("Minimum 3 players are required");
    }
}

const deletePlayerRequest = (id) => {
    $.ajax({
            method: "DELETE",
            url: `/player/delete/${id}`,
            success: function (result) {
            }
        }
    );
}

const deletePlayerHTML = (selector) => {
    let playerName = $(selector).children("input[type='text']").val();
    if (playerName !== "Bank") {
        $("#newGameModal #playerError").text("");
        $(selector).remove();
    } else {
        $("#newGameModal #playerError").text("Bank can't be removed");
    }
}

const serializeGameData = () => {
    const playerInputs = $(".input-group.playerData");
    playerInputs.each(function (index, player) {
        const id = $(player).find(`input[type='hidden']`).val();
        const name = $(player).find(`input[type='text']`).val();
        players.push({
            "id": parseInt(id),
            "name": name
        });
    });
}

const startGame = (event) => {
    event.preventDefault();
    players.length = 0;
    serializeGameData();
    $.ajax({
            method: "POST",
            url: "/game/new",
            data: JSON.stringify(players),
            contentType: "application/json",
            dataType: "json",
            success: function (gameId) {
                newGameModalObject.hide();
                getGameState(gameId);
            }
        }
    )
}