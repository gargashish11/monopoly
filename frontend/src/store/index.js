import {configureStore} from "@reduxjs/toolkit";
import {payeesReducer, resetPayees, updatePayees} from "@/store/slices/payeesSlice.js";

export const store = configureStore({
    reducer: {
        payees: payeesReducer
    }
})

export {updatePayees, resetPayees}