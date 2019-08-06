import React, { Component } from 'react';
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
            launch: {},
            loading: true
        };
        this.getLaunch = this.getLaunch.bind(this);
    }

    componentDidMount() {
        this.getLaunch();
    }

    getLaunch(){
        axios
            .get("/api//launch/" + this.props.match.params.launchId)
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
          <div>
            <div className='sweet-loading'>
                 <FadeLoader
                   sizeUnit={"px"}
                   size={100}
                   color={'#135f38'}
                   loading={this.state.loading}
                 />
               </div>
              Launch goes here
          </div>
        );
      }

}

export default withRouter(Launch);
