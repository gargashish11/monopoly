import Transaction from "@/components/Transaction.jsx";

const Transactions = ({transactions}) => {
    // console.log(transactions)
    return (
        <div className="flex flex-col mt-5 gap-4">
            {transactions.map(transaction => <Transaction key={transaction.id} transaction={transaction}/>)}
        </div>
    );
};

export default Transactions;
