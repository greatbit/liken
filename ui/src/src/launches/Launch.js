import React, { Component } from 'react';
import Alerts from '../common/Alerts';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';
import axios from "axios";
import * as Utils from '../common/Utils';
import { FadeLoader } from 'react-spinners';

import $ from 'jquery';


class Launch extends Component {

    constructor(props) {
        super(props);
        this.state = {
            launch: {
                testcases: []
            },
            loading: true
        };
        this.getLaunch = this.getLaunch.bind(this);
    }

    componentDidMount() {
        this.getLaunch();
    }

    getLaunch(){
        axios
            .get("/api/launch/" + this.props.match.params.launchId)
            .then(response => {
                 this.state.launch = response.data;
                 this.state.loading = false;
                 this.setState(this.state);
        }).catch(error => {
            Utils.onErrorMessage("Couldn't get launch: ", error);
            this.state.loading = false;
            this.setState(this.state);
        });
    }


    render() {
        return (
          <div className='container-fluid'>
            <Alerts/>
            <div className='sweet-loading'>
                 <table class="table table-striped">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Name</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.launch.testcases.map(function(testcase, i){
                            return (
                                   <tr>
                                       <td>{i + 1}</td>
                                       <td>
                                            <Link to={'/launch/' + this.props.match.params.launchId + '/' + testcase.uuid}>
                                                {testcase.name}
                                            </Link>
                                       </td>
                                       <td>{testcase.status}</td>
                                   </tr>
                                   );
                        }.bind(this))
                    }
                    </tbody>
                 </table>
                 <FadeLoader
                   sizeUnit={"px"}
                   size={100}
                   color={'#135f38'}
                   loading={this.state.loading}
                 />
               </div>
          </div>
        );
      }

}

export default withRouter(Launch);
