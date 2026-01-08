import React from 'react'
import ReactDOM from 'react-dom/client'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import {QueryClient, QueryClientProvider} from "@tanstack/react-query";
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import {HomeLayout, Landing, NewGame, RecentGames, SinglePageError} from "@/pages/index.js";
import Game from "@/pages/Game.jsx";
import {Provider} from "react-redux";
import {store} from "@/store/index.js";
import PlayerList from "@/pages/PlayerList.jsx";

const queryClient = new QueryClient();

const router = createBrowserRouter([
    {
        path: '/',
        element: <HomeLayout/>,
        errorElement: <Error/>,
        children: [
            {
                index: true,
                element: <Landing/>,
                errorElement: <SinglePageError/>
            },
            {
                path: '/game/all',
                element: <RecentGames/>,
                errorElement: <SinglePageError/>
            },
            {
                path: '/game/new',
                element: <NewGame/>,
                errorElement: <SinglePageError/>
            },
            {
                path: '/game/:id',
                element: <Game/>,
                errorElement: <SinglePageError/>
            },
            {
                path: '/players',
                element: <PlayerList/>,
                errorElement: <SinglePageError/>
            }
        ]
    }
]);

createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <Provider store={store}>
            <QueryClientProvider client={queryClient}>
                <RouterProvider router={router}>
                    <App/>
                </RouterProvider>
            </QueryClientProvider>
        </Provider>
    </React.StrictMode>
)
