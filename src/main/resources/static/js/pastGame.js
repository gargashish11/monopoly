const appendOldGameContainer = () => {
    $("#root").append(`
            <div class="container old-game-container">
                <div class="btn text-center pastGamesLabel">Past Games</div>
                <div class="pastGameList">
                    <div class="pastGameHeader">
                        <div>Game</div>
                        <div>Last Played</div>
                    </div>
                </div>
            </div>
    `);
}

const appendPastGames = () => {
    $.get({
        url: "/game/all",
        success: function (result) {
            $.each(result, function (idx, game) {
                $(".pastGameList").append(getGameMarkUp(game));
            })
        }
    })
}


const formatDate = (dateString) => {
    let dateTime = new Date(dateString);
    const year = dateTime.getFullYear();
    const month = (dateTime.getMonth() + 1).toString().padStart(2, '0'); // Months are zero-based, so we add 1
    const day = dateTime.getDate().toString().padStart(2, '0');
    const hours = dateTime.getHours().toString();
    const minutes = dateTime.getMinutes().toString().padStart(2, '0');
    return `${day}-${month}-${year} ${hours}:${minutes}`;
}

const getGameMarkUp = ({lastModifiedDate, name, id}) => {
    lastModifiedDate = formatDate(lastModifiedDate);
    return `
        <div class="pastGame">
            <input type="hidden" value=${id} />
            <div class="pastGameName">${name}</div>
            <div class="pastGameDate">${lastModifiedDate}</div>
        </div>
        `;
}

const loadPastGame = (event) => {
    let gameId = $(event.currentTarget).children("input").val();
    getGameState(gameId);
}
