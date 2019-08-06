import React, { Component } from 'react';
import Header from './common/Header'
import Main from './common/Main'
import Footer from './common/Footer'
import './App.css';
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faStroopwafel } from '@fortawesome/free-solid-svg-icons'

library.add(faStroopwafel)

class App extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
          <div>
            <Header/>
            <div className="container-fluid">
                <Main />
            </div>
            <Footer />
          </div>

        );
      }

}

export default App;
