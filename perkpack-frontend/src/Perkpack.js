import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import './Perkpack.css';

class PerkList extends Component {
  constructor(props) {
    super(props);

    this.state = {
      name: props.name,
      perks: [],
    }

    fetch('http://perkpack.herokuapp.com/perks')
      .then(result => result.json())
      .then(data => {
        console.log(data);
        this.setState({perks: data._embedded.perks,});
      });
  }
  render() {
    return (
      <div className="perk-list">
        <div className="name">
          {this.state.name}
        </div>
        <div className="list">
          <ul>
            {this.state.perks.map(perk => <li key={perk.name}>{perk.name}</li>)}
          </ul>
        </div>
      </div>
    );
  }
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
