import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom'
import Launches from './launches/Launches'
import Launch from './launches/Launch'
import Testcase from './launches/Testcase'
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
        return(
            <main>
                <Switch>
                  <Route exact path='/' component={Launches}/>
                  <Route exact path='/launch/'
                      render={(props) => <Launches {...props}  /> }/>
                  <Route exact path='/launch/:launchId' render={(props) => <Launch {...props} /> }/>
                  <Route exact path='/launch/:launchId/:testcaseUuid' render={(props) => <Testcase {...props} /> }/>
                </Switch>
            </main>
        )
      }

}

export default App;
