const randomColor = {
  r: Math.floor(Math.random() * 255),
  g: Math.floor(Math.random() * 255),
  b: Math.floor(Math.random() * 255),
};

function logout() {
  console.log("On logout()");
  window.location.assign(window.location.href + "logout");
}

function clearBoard() {
  axios.get("/clear");
}

// Retorna la url del servicio. Es una función de configuración.
function BBServiceURL() {
  return "ws://" + window.location.host + "/boardService";
}

class WSBBChannel {
  constructor(URL, callback, resetCallback) {
    this.URL = URL;
    this.wsocket = new WebSocket(URL);
    this.wsocket.onopen = (evt) => this.onOpen(evt);
    this.wsocket.onmessage = (evt) => this.onMessage(evt);
    this.wsocket.onerror = (evt) => this.onError(evt);
    this.receivef = callback;
    this.clearCallback = resetCallback;
  }

  onOpen(evt) {
    console.log("In onOpen", evt);
  }

  onMessage(evt) {
    console.log("In onMessage", evt);
    // Este if permite que el primer mensaje del servidor no se tenga en cuenta.
    // El primer mensaje solo confirma que se estableció la conexión.
    // De ahí en adelante intercambiaremos solo puntos(x,y) con el servidor

    if (evt.data != "Connection established.") {
      if (evt.data === "reset") {
        console.log("Reset from server");
        this.clearCallback();
      } else {
        this.receivef(evt.data);
      }
    }
  }

  onError(evt) {
    console.error("In onError", evt);
  }

  send(point) {
    console.log("sending: ", JSON.stringify(point));
    this.wsocket.send(JSON.stringify(point));
  }
}

class BBCanvas extends React.Component {
  constructor(props) {
    super(props);
    this.comunicationWS = new WSBBChannel(
      BBServiceURL(),
      (msg) => {
        let point = JSON.parse(msg);
        console.log("On func call back ", msg);
        this.drawPoint(point);
      },
      () => {
        this.reset();
      }
    );
    this.myp5 = null;
    this.state = { loadingState: "Loading Canvas ..." };
    let wsreference = this.comunicationWS;
    this.sketch = function (p) {
      p.setup = function () {
        p.createCanvas(window.innerWidth - 200, window.innerHeight - 100);
        p.background(255, 255, 255);
      };

      p.draw = function () {
        if (p.mouseIsPressed === true) {
          p.fill(randomColor.r, randomColor.g, randomColor.b);
          p.noStroke();
          p.ellipse(p.mouseX, p.mouseY, 3, 3);
          console.log(p.mouseX, p.mouseY);
          wsreference.send({
            x: p.mouseX,
            y: p.mouseY,
            color: randomColor,
          });
        } else {
          p.fill(255, 255, 255);
        }
      };
    };
  }

  reset() {
    this.myp5.clear();
    this.myp5.background(255, 255, 255);
  }

  drawPoint(point) {
    console.log(JSON.stringify(point));
    this.myp5.fill(point.color.r, point.color.g, point.color.b);
    this.myp5.noStroke();
    this.myp5.ellipse(point.x, point.y, 3, 3);
  }

  componentDidMount() {
    this.myp5 = new p5(this.sketch, "container");
    this.setState({ loadingState: "Canvas Loaded" });
  }

  render() {
    return <div></div>;
  }
}

class Editor extends React.Component {
  render() {
    return (
      <div id="container">
        <BBCanvas />
      </div>
    );
  }
}

ReactDOM.render(<Editor />, document.getElementById("root"));
