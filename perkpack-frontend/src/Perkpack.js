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

    this.upvote = this.upvote.bind(this);
    this.downvote = this.downvote.bind(this);

    fetch('http://perkpack.herokuapp.com/perks')
      .then(result => result.json())
      .then(data => {
        console.log(data);
        this.setState({perks: data._embedded.perks,});
      });
  }

  vote(event, increment) {
    console.log(event.currentTarget.dataset.name);

    fetch('http://perkpack.herokuapp.com/score', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        name: event.currentTarget.dataset.name,
        score: increment,
      }),
    })
  }

  upvote(event) {
    this.vote(event, 1);
  }

  downvote(event) {
    this.vote(event, -1);
  }

  render() {
    return (
      <div className="perk-list">
        <div className="name">
          {this.state.name}
        </div>
        <div className="list">
          <ul>
            {this.state.perks.map(perk => <li key={perk.name}>
              <div>
                <button className="up" data-name={perk.name} onClick={this.upvote}>+</button>
                <button className="down" data-name={perk.name} onClick={this.downvote}>-</button>
                {perk.score}
              </div>
              <div>
                {perk.name}
              </div>
              <div>
                {perk.description}
              </div>
            </li>)}
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
