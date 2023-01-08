const Link = ReactRouterDOM.Link;
const Route = ReactRouterDOM.Route;

const App = () => (
        <ReactRouterDOM.HashRouter>
        <ul>
        <li><Link to="/">Home</Link></li>
        <li><Link to="/login">Login</Link></li>
        <li><Link to="/register">Register</Link></li>
        </ul>
        <Route path="/" exact component={Home} />
        <Route path="/login" component={Login} />
        <Route path="/register" component={Register} />
        </ReactRouterDOM.HashRouter>
);


const Home = () => <h1>Home</h1>;
const Login = () => <h1>Login</h1>;
const Register = () => <h1>Register</h1>;

const rootElement = document.getElementById("root");
ReactDOM.createRoot(rootElement).render(<App />);
