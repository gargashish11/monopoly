const links = [
    {
        label: 'game',
        hrefs: [
            {
                to: '/game/new',
                label: 'New Game'
            },
            {
                to: '/game/all',
                label: 'Recent Games'
            },
        ]
    },
    {
        label: 'player',
        hrefs: [
            {
                to: '/player/edit',
                label: 'Add/Delete/Edit Player'
            },
            {
                to: '/player/view',
                label: 'View Players'
            },
        ]
    },
    {
        label: 'transaction',
        hrefs: [
            {
                to: '/transaction/new',
                label: 'New Transaction'
            },
            {
                to: '/transaction/all',
                label: 'View Transactions'
            }
        ]
    }
]

export default links