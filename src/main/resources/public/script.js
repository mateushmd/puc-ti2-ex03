const url = '/game';

const inputs = {};

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
        const publishDate = inputs['publishDate'].value;

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
            addTable(game);
        });
    });

    document.querySelector('#clear-button').addEventListener('click', e =>
    {
        e.preventDefault();
        Object.entries(inputs).forEach(entry => entry[1].value = '');
    });

    getAll(list =>
    {
        registerAll(list);
    });
});

function getAll(callback)
{
    fetch(`${url}/list/any-order`)
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

function remove(id, callback)
{
    fetch(`${url}/delete/${id}`)
        .then(response => validateResponse(response))
        .then(data =>
        {
            Object.entries(inputs).forEach(entry => entry[1].value = '');
            callback();
            window.alert(data.success);
        })
        .catch(err => window.alert(err.message));
}

async function validateResponse(response)
{
    const data = await response.json();

    if (!response.ok)
        throw new Error(data.error);

    return data;
}

function populateInputs(game)
{
    Object.entries(game).forEach(entry =>
    {
        try 
        {
            inputs[entry[0]].value = entry[1];
        }
        catch (error)
        {

        }
    });
}

function registerAll(entries)
{
    entries.forEach(game =>
    {
        let value = parseFloat(game.price);
        value = parseFloat(value.toFixed(2));
        game.price = value;
        game.publishDate = formatDate(game.publishDate);
        addTable(game);
    });
}

function addTable(game)
{
    const row = document.createElement('tr');

    row.innerHTML = `
        <th scope="row">${game.id}</th>
        <td>${game.title}</td>
        <td>${game.genre}</td>
        <td>${game.developer}</td>
        <td>${game.publisher}</td>
        <td>${game.price}</td>
        <td>${formatDateReadable(game.publishDate)}</td>
        <td id="delete">🗑️</td>
    `;

    document.querySelector('tbody').appendChild(row);

    row.addEventListener('click', e =>
    {
        populateInputs(game);
    });

    row.querySelector('#delete').addEventListener('click', e =>
    {
        remove(game.id, () => row.remove());
    });
}

function formatDate(date)
{
    if (date[1] < 10) date[1] = `0${date[1]}`;
    if (date[2] < 10) date[2] = `0${date[2]}`;

    return `${date[0]}-${date[1]}-${date[2]}`;
}

function formatDateReadable(date)
{
    const a = date.split('-');
    return `${a[2]}/${a[1]}/${a[0]}`;
}