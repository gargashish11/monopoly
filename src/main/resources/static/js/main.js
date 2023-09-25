$(document).ready(function () {

    $("#root").children().remove();

    $("#root").append(`
    <div class="container new-game-container">
        <button class="btn btn-primary newGame"
                data-bs-toggle="modal"
                data-bs-target="#newGameModal">
            New Game
        </button>
    </div>
    `);

    const updateInput = (event) => {
        players.length = 0;
        serializeGameData();
    };

    $(document).on('click', ".newGame", newGameModal);
    $(document).on('click', ".playerAdd", addPlayerHTML);
    $(document).on('click', ".playerRemove", deletePlayer);
    $(document).on('click', "#startGame", startGame);
    $(document).on('change', 'input', updateInput);
})