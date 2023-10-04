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
    let playerId = $(event.currentTarget).siblings(".hiddenId").val();
    // if (playerId > 0) {
    //     deletePlayerRequest(playerId);
    // }
    deletePlayerHTML($(event.currentTarget).parent());
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
    $(selector).remove();
}

const serializeGameData = () => {
    const playerInputs = $(".input-group.playerData");
    playerInputs.each(function (index) {
        const id = $(`input[id^="player[${index}].id"]`).val();
        const name = $(`input[id^="player[${index}].name"]`).val();
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