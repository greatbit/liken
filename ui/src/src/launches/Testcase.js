import React, { Component } from 'react';
import Alerts from '../common/Alerts';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';
import axios from "axios";
import * as Utils from '../common/Utils';
import { FadeLoader } from 'react-spinners';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faInfoCircle } from '@fortawesome/free-solid-svg-icons'


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
        this.onSelectTestcase = this.onSelectTestcase.bind(this);
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
                 this.updateTestcaseStatus("RUNNING");
                 this.setState(this.state);
        }).catch(error => {
            Utils.onErrorMessage("Couldn't get testcase: ", error);
            this.state.loading = false;
            this.setState(this.state);
        });
    }

    updateTestcaseStatus(status, event){
         axios
            .post("/api/launch/" + this.props.match.params.launchId + "/" + this.state.testcase.uuid + "?status=" + status)
            .then(response => {
                 this.state.testcase = response.data;
                 this.setState(this.state);
        }).catch(error => {
            Utils.onErrorMessage("Couldn't update testcase status: ", error);
            this.state.loading = false;
            this.setState(this.state);
        });
    }

    onSelectTestcase(event){
        this.updateTestcaseStatus("RUNNABLE", event);
        window.location.href = "/launch/" + this.state.launch.id  + "/" + event.target.value;
    }


    switchFrames(){
        $('#frame-a').toggle();
        $('#frame-b').toggle();

        $('#url-a').toggleClass("selected-url");
        $('#url-b').toggleClass("selected-url");
    }

    render() {
        return (
          <div className="container-fluid testcase">
            <nav className="navbar row">
                <div id="url-a" className="col-2 selected-url testcase-head-url">
                    <a href={this.state.testcase.urlA || ""} target="_blank">
                        {this.state.testcase.urlA}
                    </a>
                </div>

                <div className="col-4">
                    <select id="testcases-select" onChange={this.onSelectTestcase}>
                        {
                            this.state.launch.testcases.map(function(testcase, i){
                                if (this.state.testcase.uuid == testcase.uuid){

                                    return (
                                       <option value={testcase.uuid} selected>
                                            {testcase.name}
                                       </option>
                                    );
                                } else {

                                    return (
                                        <option value={testcase.uuid}>
                                            {testcase.name}
                                        </option>
                                    );
                                }
                            }.bind(this))
                        }
                    </select>
                    <FontAwesomeIcon icon={faInfoCircle} className="float-right clickable"
                                data-toggle="modal" data-target="#remove-testcase-confirmation"/>
                </div>

                <div id="url-b" className="col-2 testcase-head-url">
                    <a href={this.state.testcase.urlB || ""} target="_blank">
                        {this.state.testcase.urlB}
                    </a>
                </div>

                <div className="col-4" >
                    <button type="button" className="btn btn-primary switch-btn"
                                onClick={this.switchFrames}>Switch</button>
                    <div class="btn-group float-right" role="group">
                      <button type="button" className="btn btn-success"
                                onClick={(e) => this.updateTestcaseStatus("PASSED", e)}>Pass</button>
                      <button type="button" className="btn btn-danger"
                                onClick={(e) => this.updateTestcaseStatus("FAILED", e)}>Fail</button>
                      <button type="button" className="btn btn-warning"
                                onClick={(e) => this.updateTestcaseStatus("BROKEN", e)}>Broken</button>
                      <button type="button" className="btn btn-light"
                                onClick={(e) => this.updateTestcaseStatus("SKIPPED", e)}>Skip</button>
                    </div>
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
            <div id="frame-a" className="frame">
              <iframe src={this.state.testcase.urlA} className="frame"></iframe>
            </div>
            <div id="frame-b" style={{display: 'none'}} className="frame">
              <iframe src={this.state.testcase.urlB} className="frame"></iframe>
            </div>


            <div className="modal fade" tabIndex="-1" role="dialog" id="remove-testcase-confirmation">
                  <div className="modal-dialog" role="document">
                      <div className="modal-content">
                        <div className="modal-header">
                          <h5 className="modal-title">{this.state.testcase.name}</h5>
                          <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                          </button>
                        </div>
                        <div className="modal-body">{this.state.testcase.description}</div>
                        <div className="modal-footer">
                          <button type="button" className="btn btn-secondary" data-dismiss="modal" aria-label="Cancel">Close</button>
                        </div>
                      </div>
                   </div>
               </div>
          </div>
        );
      }

}

export default withRouter(Testcase);
