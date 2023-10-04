let currentGame;
$(document).ready(function () {
    $("#root").children().remove();

    const appendNewGameContainer = () => {
        $("#root").append(`
            <nav class="container new-game-container sticky-top">
                <button class="btn btn-primary newGame"
                    data-bs-toggle="modal"
                    data-bs-target="#newGameModal">
                    New Game
                </button>
            </nav>
        `);
    }


    const updateInput = (event) => {
        players.length = 0;
        serializeGameData();
    };
    appendNewGameContainer();
    appendOldGameContainer();
    appendPastGames();

    $(document).on('click', ".newGame", newGameModal);
    // $(document).on('click', ".newGame", showGameState);
    $(document).on('click', ".playerAdd", addPlayerHTML);
    $(document).on('click', ".playerRemove", deletePlayer);

    $(document).on('click', "#startGame", startGame);
    $(document).on('click', "#changeGameName", changeGameName)
    $(document).on('change', '.playerData input', updateInput);

    $(document).on('click', '.pastGame', loadPastGame);

    $(document).on('shown.bs.modal', '#newTransactionModal', addPlayersToTransactionModal)
    $(document).on('click', '#addTransaction', addTransaction);
})