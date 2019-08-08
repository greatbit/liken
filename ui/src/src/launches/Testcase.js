import React, { Component } from 'react';
import Alerts from '../common/Alerts';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';
import axios from "axios";
import * as Utils from '../common/Utils';
import { FadeLoader } from 'react-spinners';

import $ from 'jquery';


class Testcase extends Component {

    constructor(props) {
        super(props);
        this.state = {
            launch: { testcases: []},
            testcase: {},
            loading: true
        };
        this.getTestcase = this.getTestcase.bind(this);
        this.updateTestcaseStatus = this.updateTestcaseStatus.bind(this);
    }

    componentDidMount() {
        this.getTestcase();
    }

    getTestcase(){
        axios
            .get("/api/launch/" + this.props.match.params.launchId)
            .then(response => {
                 this.state.launch = response.data;
                 this.state.testcase = this.state.launch.testcases.find(testcase => testcase.uuid == this.props.match.params.testcaseUuid);
                 this.state.loading = false;
                 this.setState(this.state);
        }).catch(error => {
            Utils.onErrorMessage("Couldn't get testcase: ", error);
            this.state.loading = false;
            this.setState(this.state);
        });
    }

    updateTestcaseStatus(status, event){
        //ToDO: implement
    }


    switchFrames(){
        //ToDO: implement
    }

    render() {
        return (
          <div className='container-fluid'>
            <nav className="navbar navbar-expand-md navbar-dark">
                <a href={this.state.testcase.urlA || ""} target="_blank">
                    {this.state.testcase.urlA}
                </a>

                 <select id="testcases-select">
                    {
                        this.state.launch.testcases.map(function(testcase, i){
                            return (
                                   <option value={testcase.uuid}>
                                        {testcase.name}
                                   </option>
                                   );
                        }.bind(this))
                    }
                 </select>
                <span id="info">info</span>

                <a href={this.state.testcase.urlB || ""} target="_blank">
                    {this.state.testcase.urlB}
                </a>


                <button type="button" className="btn button-primary"
                        onClick={this.switchFrames}>Switch</button>

                <div class="btn-group" role="group">
                  <button type="button" className="btn button-success"
                            onClick={(e) => this.updateTestcaseStatus("PASSED", e)}>Pass</button>
                  <button type="button" className="btn button-danger"
                            onClick={(e) => this.updateTestcaseStatus("FAILED", e)}>Fail</button>
                  <button type="button" className="btn button-warning"
                            onClick={(e) => this.updateTestcaseStatus("BROKEN", e)}>Broken</button>
                  <button type="button" className="btn button-light"
                            onClick={(e) => this.updateTestcaseStatus("SKIPPED", e)}>Skip</button>
                </div>

              </nav>
            <Alerts/>
            <div className='sweet-loading'>
                 <FadeLoader
                   sizeUnit={"px"}
                   size={100}
                   color={'#135f38'}
                   loading={this.state.loading}
                 />
               </div>
              <div id="frame-a">
                <iframe src={this.state.testcase.urlA}></iframe>
              </div>
              <div id="frame-b">
                <iframe src={this.state.testcase.urlB}></iframe>
              </div>
          </div>
        );
      }

}

export default withRouter(Testcase);
