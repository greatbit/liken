import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom'
import Launches from '../launches/Launches'
import Launch from '../launches/Launch'

class Main extends Component {

    render() {
        return(
            <main>
                <div className="row justify-content-end">
                    <div className="alert alert-success alert-message col-4" id="success-alert">
                        <span id="success-message-text"></span>
                    </div>
                    <div className="alert alert-danger alert-message col-4" id="error-alert" >
                        <span id="error-message-text"></span>
                    </div>
                </div>

                <Switch>
                  <Route exact path='/' component={Launches}/>
                  <Route exact path='/launch/'
                      render={(props) => <Launches {...props}  /> }/>
                  <Route exact path='/launch/:launchId'
                      render={(props) => <Launch {...props}  onProjectChange={this.onProjectChange.bind(this)} /> }/>
                </Switch>
            </main>
        );
    }
}

export default Main;
