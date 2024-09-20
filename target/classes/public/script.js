const url = '/game';

const inputs = {};

const games = [];

window.addEventListener('load', () =>
{
    document.querySelectorAll('input,textarea').forEach(input => 
    {
        inputs[input.id] = input;
    });

    inputs['price'].addEventListener('input', e =>
    {
        const input = e.target;

        let value = input.value.replace(/[^0-9]/g, '');

        if (value)
            input.value = (parseFloat(value) / 100).toFixed(2).replace('.', ',');
    });

    document.querySelector('#add-button').addEventListener('click', e =>
    {
        e.preventDefault();

        const title = inputs['title'].value;
        const description = inputs['description'].value;
        const genre = inputs['genre'].value;
        const developer = inputs['developer'].value;
        const publisher = inputs['publisher'].value;
        const price = inputs['price'].value.replace(',', '.');
        const publishDate = inputs['publish-date'].value;

        const game = {
            title: title,
            description: description,
            genre: genre,
            developer: developer,
            publisher: publisher,
            price: price,
            publishDate: publishDate
        };

        insert(game, id =>
        {
            game.id = id;
            games.push(game);
            console.log(game);
        });
    });

    document.querySelector('#clear-button').addEventListener('click', e =>
    {
        e.preventDefault();
        Object.entries(inputs).forEach(entry => entry[1].value = '');
    });

    getAll(list => console.log(list));
});


function getAll(callback)
{
    fetch(`${url}/list`)
        .then(response => validateResponse(response))
        .then(data => callback(data))
        .catch(err => window.alert(err.message));
}

function insert(game, callback)
{
    fetch(`${url}/insert`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(game)
    })
        .then(response => validateResponse(response))
        .then(data => callback(data))
        .catch(err => window.alert(err.message));
}

async function validateResponse(response)
{
    const data = await response.json();

    if (!response.ok)
        throw new Error(data.error);

    return data;
}