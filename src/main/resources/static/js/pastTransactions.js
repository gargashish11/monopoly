const pastTransactionsMarkUp = ({payer_name, payee_name, amount, createdDate}) => {
    return `
    <div class="txn text-center shadow" data-bs-toggle="modal"
                    data-bs-target="#newTransactionModal">
        <div class="text-success">
            <i class="fa-solid fa-circle-plus"></i>
        </div>
        <div class="txnSummary">
            <span class="mx-1">${payer_name}</span> 
            <span class="mx-1">Paid</span> 
            <span class="mx-1">${payee_name}</span> 
            <span class="mx-1">${amount}</span> 
        </div>
        <sub class="ms-2">${formatDate(createdDate)}</sub>
    </div>
 `;
}

const showPastTransactions = (transactions) => {
    transactions.sort((a, b) => {
        const dateA = new Date(a.createdDate);
        const dateB = new Date(b.createdDate);
        return dateB - dateA;
    });


    $.each(transactions, function (idx, transaction) {
        $("#root").append(pastTransactionsMarkUp(transaction));
    })
}