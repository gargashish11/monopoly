let navBar = `
    <nav class="navbar" data-bs-theme="light">
    </nav>
`;

let playerBalanceGroup = `
    <div class="playerBalanceGroup">
    </div>
 `;

const getGameState = (gameId) => {
    $.ajax({
        method: "GET", url: `/game/${gameId}`, success: function (game) {
            currentGame = game;
            showGameState(currentGame);
        }
    })
}

const showGameState = (game) => {
    $("#root").children().remove();
    $("#root").append(navBar);
    $(".navbar").append(gameNameMarkUp({id: game.id, name: game.name}));
    $(".navbar").append(playerBalanceGroup);
    $.each(game.gamePlayers, function (idx, gamePlayer) {
        $(".playerBalanceGroup").append(playerBalance(idx, gamePlayer));
    });
    showPastTransactions(game.transactions);
}

const gameNameMarkUp = ({id, name}) => {
    return `
    <div id="gameName" class="input-group input-group-lg">
        <span class="input-group-text">Game Name</span>
        <input id="gameId" type="hidden" value="${id}"/>
        <input id="gameName" type="text" class="form-control" value="${name}"/>
        <span id="changeGameName" class="input-group-text">Save</span>
    </div>
    `;
}

const changeGameName = (event) => {
    let gameId = $(event.currentTarget).parent().children("#gameId").val();
    let gameName = $(event.currentTarget).parent().children("#gameName").val();
    let gameNameData = {
        id: gameId, name: gameName
    }
    $.ajax({
        method: "PUT",
        url: `/game/save`,
        data: JSON.stringify(gameNameData),
        contentType: "application/json",
        dataType: "json",
        success: function () {
            getGameState(gameId);
        }
    })
}

const playerBalance = (idx, {id, player_name, balance}) => {
    return `
    <div class="playerBalance shadow text-center" data-bs-toggle="modal"
                    data-bs-target="#newTransactionModal" >
        <input id="player[${idx}].id" class="hiddenId" type="hidden" value="${id}"/>
        <div>${player_name}</div>
        <div>${balance}</div>
    </div>
 `;
}

