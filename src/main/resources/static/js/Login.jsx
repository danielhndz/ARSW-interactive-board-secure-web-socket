function getLoginURL() {
  return window.location.href;
}

async function login(data) {
  console.log("login", window.location.href, data);
  return fetch(getLoginURL(), {
    method: "POST",
    body: JSON.stringify(data),
  });
}

class LoginForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = { username: "", password: "" };

    this.handleChangeUsername = this.handleChangeUsername.bind(this);
    this.handleChangePassword = this.handleChangePassword.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleChangeUsername(evt) {
    console.log("handleChangeUsername", evt.target.value);
    this.setState({ username: evt.target.value });
  }

  handleChangePassword(evt) {
    console.log("handleChangePassword", evt.target.value);
    this.setState({ password: evt.target.value });
  }

  handleSubmit(evt) {
    console.log("handleSubmit", this.state);
    login(this.state);
    evt.preventDefault();
    this.setState({ username: "", password: "" });
  }

  render() {
    return (
      <div>
        <form onSubmit={this.handleSubmit}>
          <label>Username</label>
          <input
            type="text"
            value={this.state.username}
            onChange={this.handleChangeUsername}
            required
          />
          <label>Password</label>
          <input
            type="password"
            value={this.state.password}
            onChange={this.handleChangePassword}
            required
          />
          <input type="submit" value="Login" />
        </form>
      </div>
    );
  }
}

ReactDOM.render(<LoginForm />, document.getElementById("root"));
