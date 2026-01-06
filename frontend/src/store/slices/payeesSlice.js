import {createSlice} from "@reduxjs/toolkit";

const payeesSlice = createSlice({
    name: "payees",
    initialState: [],
    reducers: {
        updatePayees: (payees, action) => {
            payees.push(...action.payload);
        },
        resetPayees: () => [],
    }
})

export const {updatePayees, resetPayees}
    = payeesSlice.actions;
export const payeesReducer = payeesSlice.reducer;