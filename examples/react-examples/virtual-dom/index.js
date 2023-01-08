const rootElement = document.getElementById("root");
const root = ReactDOM.createRoot(rootElement);

const update = () => {
    const element = (
            <>
            <p>Éste párrafo y el campo de texto de abajo son elementos del DOM, aparecen como nodos en el Arbol Virtual del DOM de ReactJS</p>
            <p>Estos dos elementos NO se van a renderizar cuando se ejecute la función setInterval que actualiza la hora..</p>
            <input type="text" />

            <p>La hora está contenida en un elemento span del DOM y figura como otro nodo más en el Árbol Virtual DOM de ReactJS</p>
            <p>cuando se ejecute la función setInterval, sólo se actualizará el texto SPAN</p>
            <span>Hora: {new Date().toLocaleTimeString()}</span>
            </>
    );
    root.render(element);
};

setInterval(update, 1000);

