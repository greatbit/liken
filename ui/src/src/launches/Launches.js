import React, { Component } from 'react';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';
import axios from "axios";
import * as Utils from '../common/Utils';
import { FadeLoader } from 'react-spinners';

import $ from 'jquery';


class Launches extends Component {
    state = {
        launches: [],
        loading: true
    };
    constructor(props) {
        super(props);
        this.getLaunched = this.getLaunches.bind(this);
    }

    componentDidMount() {
        this.getLaunches();
        this.interval = setInterval(this.getLaunches, 30000);
    }

    getLaunches(){
        axios
            .get("/api/launch")
            .then(response => {
                 this.state.launches = response.data;
                 this.state.loading = false;
                 this.setState(this.state);
        }).catch(error => {
            Utils.onErrorMessage("Couldn't get launches: ", error);
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

export default withRouter(Launches);