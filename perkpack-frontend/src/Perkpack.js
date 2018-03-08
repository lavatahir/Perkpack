import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import './Perkpack.css';

function PerkList(props) {
  return (
      <div>
          {props.name}
      </div>
  );
}

class Perkpack extends Component {
  constructor(props) {
    super(props);

    const welcomeMessage = this.props.name === undefined ? 'Welcome!' : 'Welcome, ' + this.props.name + '!';

    this.state = {
      welcomeMessage: welcomeMessage,
    }
  }

  render() {
    return (
      <div className="Perkpack">
        <h2>Perkpack</h2>
        <div className="welcome">{this.state.welcomeMessage}</div>
        <PerkList
            name="Top Perks Today"
        />
        <PerkList
            name="Recommended for You"
        />
      </div>
    );
  }
}

export default Perkpack;
