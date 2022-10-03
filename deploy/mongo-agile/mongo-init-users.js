db = db.getSiblingDB('trello_clone_db');
db.createUser(
    {
        user: 'Tr3lL0_cL0n3',
        pwd: '53cr3t_P455W0rd',
        roles: [{role: 'readWrite', db: 'trello_clone_db'}],
    },
);
