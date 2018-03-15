import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Perkpack from './Perkpack';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(<Perkpack name="Vanja" />, document.getElementById('root'));
registerServiceWorker();
