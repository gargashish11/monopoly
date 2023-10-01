let dummyGameDataReceived = {
    "id": 952,
    "name": "New Game",
    "gamePlayers": [
        {
            "id": 802,
            "balance": 1500
        },
        {
            "id": 803,
            "balance": 1500
        },
        {
            "id": 804,
            "balance": 1500
        },
        {
            "id": 805,
            "balance": 1500
        }
    ],
    "transactions": []
}
let navBar = `
    <nav class="navbar" data-bs-theme="light">
    </nav>
`;

let playerBalanceGroup = `
    <div class="playerBalanceGroup">
    </div>
 `;

const showGameState = () => {
    console.log(dummyGameDataReceived);
    $("#root").children().remove();
    $("#root").append(navBar);
    $(".navbar").append(gameNameMarkUp(
        {
            id: dummyGameDataReceived.id,
            name: dummyGameDataReceived.name
        }
    ));
    $(".navbar").append(playerBalanceGroup);
    $(".playerBalanceGroup").append(playerBalance(
        1, {id: 1, name: 'Jaivik', balance: 2000}
    ));
    $("#root").append(txn());
}

const gameNameMarkUp = ({id, name}) => {
    return `
    <div id="gameName" class="input-group input-group-lg">
        <span class="input-group-text">Game Name</span>
        <input id="${id}" type="hidden" value="${id}"/>
        <input type="text" class="form-control" value="${name}"/>
        <span class="input-group-text">Save</span>
    </div>
    `;
}

const playerBalance = (idx, {id, name, balance}) => {
    return `
    <div class="playerBalance shadow text-center">
        <input id="player[${idx}].id" class="hiddenId" type="hidden" value="${id}"/>
        <div>${name}</div>
        <div>${balance}</div>
    </div>
 `;
}

const txn = () => {
    return `
    <div class="txn text-center shadow">
        <div class="text-success">
            <i class="fa-solid fa-circle-plus"></i>
        </div>
        <div class="txnSummary">
            Bank Paid Ashish 200
        </div>
        <div class="text-danger">
            <i class="fa-solid fa-circle-minus"></i>
        </div>
    </div>
 `;
}