let game = {
    "name": "new",
    "gamePlayers": [],
    "transactions": []
};

let newGame = {
    "name": "new",
    "gamePlayers": [
        {
            "id": 1,
            "balance": 1500
        },
        {
            "id": 2,
            "balance": 1500
        },
        {
            "id": 3,
            "balance": 1500
        }
    ],
    "transactions": []
}
const setGameName = () => {
    const now = new Date();

    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0'); // Month is zero-based
    const day = String(now.getDate()).padStart(2, '0');
    const hour = String(now.getHours()).padStart(2, '0');
    const minute = String(now.getMinutes()).padStart(2, '0');
    const separator = '_';

    game.name = `${year}${separator}${month}${separator}${day}${separator}${hour}${separator}${minute}`;
}

const newGameModal = () => {
    let appendTarget = $("#newGameModal .modal-body #playerForm");
    $(appendTarget).children().remove();
    $(appendTarget).append(`<input id="game" name="game" type="hidden" value="0">`);
    $.get({
        url: "/player/all",
        success: function (result) {
            $.each(result, function (idx, player) {
                addPlayer(appendTarget, idx, player)
            })
        }
    })
}

const addPlayer = (appendTarget, idx, {id, name, balance}) => {
    let playerMarkup = `
<div class="input-group playerData">
    <div class="text-success me-2 playerAdd">
        <i class="fa-solid fa-circle-plus"></i>
    </div>
    <input id="player[${idx}].id" class="hiddenId" type="hidden" value="${id}"/>
    <input id="player[${idx}].name" class="form-control" type="text" value="${name}"/>
    <input id="player[${idx}].balance" class="form-control" type="number" value="${balance}"/>
    <div class="text-danger ms-2 playerRemove">
        <i class="fa-solid fa-circle-minus"></i>
    </div>
</div>`;
    $(appendTarget).append(playerMarkup);
}

const addPlayerHTML = (event) => {
    addPlayer($(event.currentTarget).parents("#playerForm"), 0, {id: 0, name: '', balance: 0})
}

const deletePlayer = (event) => {
    let playerId = $(event.currentTarget).siblings(".hiddenId").val();
    if (playerId > 0) {
        deletePlayerRequest(playerId);
    }
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
    const form = $("#playerForm");
    const playerInputs = $(".input-group.playerData");
    playerInputs.each(function (index) {
        const id = $(`input[id^="player[${index}].id"]`).val();
        const name = $(`input[id^="player[${index}].name"]`).val();
        const balance = $(`input[id^="player[${index}].balance"]`).val();
        game.gamePlayers.push({
            "id": parseInt(id),
            "balance": 3000
        });
    });
}

const startGame = (event) => {
    event.preventDefault();
    game.gamePlayers.length = 0;
    setGameName();
    serializeGameData();
    console.log(game);
    $.ajax({
        method: "PUT",
        url: "/game/add",
        data: JSON.stringify(game),
        contentType: "application/json"
    })
}