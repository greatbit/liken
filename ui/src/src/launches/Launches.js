import React, { Component } from 'react';
import Header from '../common/Header';
import Footer from '../common/Footer';
import Alerts from '../common/Alerts';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';
import axios from "axios";
import * as Utils from '../common/Utils';
import { FadeLoader } from 'react-spinners';

import $ from 'jquery';


class Launches extends Component {
    state = {
        launches: [],
        pager: {
            total: 0,
            current: 0,
            maxVisiblePage: 7,
            itemsOnPage: 20
        },
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
        }, this);
    }

    getProgressBar(launch){
        return (
            <div class="progress">
              <div class="progress-bar progress-bar-striped" role="progressbar" style={Utils.getProgressBarStyle(launch.launchStats.statusCounters.RUNNING, launch.launchStats.total)}></div>
              <div class="progress-bar bg-success" role="progressbar" style={Utils.getProgressBarStyle(launch.launchStats.statusCounters.PASSED, launch.launchStats.total)}></div>
              <div class="progress-bar bg-danger" role="progressbar" style={Utils.getProgressBarStyle(launch.launchStats.statusCounters.FAILED, launch.launchStats.total)}></div>
              <div class="progress-bar bg-warning" role="progressbar" style={Utils.getProgressBarStyle(launch.launchStats.statusCounters.BROKEN, launch.launchStats.total)}></div>
            </div>
        )
    }


    render() {
        return (
          <div>
              <Header/>
              <div className='container-fluid'>
                <Alerts/>
                <div className='sweet-loading'>
                     <FadeLoader
                       sizeUnit={"px"}
                       size={100}
                       color={'#135f38'}
                       loading={this.state.loading}
                     />
                   </div>
                  <table class="table table-striped">
                        <thead>
                            <tr>
                              <th scope="col">Title</th>
                              <th scope="col">Progress</th>
                              <th scope="col">Created</th>
                              <th scope="col">Started</th>
                              <th scope="col">Finished</th>
                              <th></th>
                            </tr>
                        </thead>
                        <tbody>
                        {
                              this.state.launches.map(function(launch){
                                  return (
                                         <tr>
                                             <td>
                                                  <Link to={'/launch/' + launch.id}>
                                                      {launch.name}
                                                  </Link>
                                             </td>
                                             <td>{this.getProgressBar(launch)}</td>
                                             <td>{Utils.timeToDate(launch.createdTime)}</td>
                                             <td>{Utils.timeToDate(launch.startTime)}</td>
                                             <td>{Utils.timeToDate(launch.finishTime)}</td>
                                             <td>
                                                  {launch.launcherConfig && launch.launcherConfig.launcherId &&
                                                      <FontAwesomeIcon icon={faPlug}/>
                                                  }
                                             </td>
                                         </tr>
                                         );
                              }.bind(this))
                        }
                        </tbody>
                    </table>
                    <div>
                        <Pager
                            totalItems={this.state.pager.total}
                            currentPage={this.state.pager.current}
                            visiblePages={this.state.pager.maxVisiblePage}
                            itemsOnPage={this.state.pager.itemsOnPage}
                            onPageChanged={this.handlePageChanged}
                        />
                    </div>
              </div>
              <Footer/>
          </div>
        );
      }

}

export default withRouter(Launches);
