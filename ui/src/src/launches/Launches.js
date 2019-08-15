import React, { Component } from 'react';
import Header from '../common/Header';
import Footer from '../common/Footer';
import Alerts from '../common/Alerts';
import Pager from '../common/Pager';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';
import axios from "axios";
import * as Utils from '../common/Utils';
import { FadeLoader } from 'react-spinners';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

import $ from 'jquery';


class Launches extends Component {
    state = {
        launches: [],
        filter: {
            page: 0,
            size: 20
        },
        pager: {
            total: 0,
            current: 0,
            maxVisiblePage: 7,
            itemsOnPage: 20,
            current: 0
        },
        loading: true
    };
    constructor(props) {
        super(props);
        this.getLaunched = this.getLaunches.bind(this);
        this.handlePageChanged = this.handlePageChanged.bind(this);
    }

    componentDidMount() {
        Utils.queryToFilter(this.props.location.search.substring(1));
        this.getLaunches(this);
        this.interval = setInterval(() => (this.getLaunches(this)), 30000);
    }

    getLaunches(that){
        axios
            .get("/api/launch?" + Utils.filterToQuery(this.state.filter) )
            .then(response => {
                 that.state.launches = response.data.content;
                 that.state.pager.total = response.data.totalElements;
                 that.state.pager.current = response.data.number;
                 that.state.pager.itemsOnPage = response.data.size;
                 this.state.pager.current =  this.state.filter.page;
                 that.state.loading = false;
                 that.setState(that.state);
        }).catch(error => {
            Utils.onErrorMessage("Couldn't get launches: ", error);
            that.state.loading = false;
            that.setState(that.state);
        });
    }

    getProgressBar(launch){
        return (
            <div className="progress">
              <div className="progress-bar progress-bar-striped" role="progressbar" style={Utils.getProgressBarStyle(this.getTestcasesCountByStatus(launch, 'RUNNING'), launch.testcases.length)}></div>
              <div className="progress-bar bg-success" role="progressbar" style={Utils.getProgressBarStyle(this.getTestcasesCountByStatus(launch, 'PASSED'), launch.testcases.lengthl)}></div>
              <div className="progress-bar bg-danger" role="progressbar" style={Utils.getProgressBarStyle(this.getTestcasesCountByStatus(launch, 'FAILED'), launch.testcases.length)}></div>
              <div className="progress-bar bg-warning" role="progressbar" style={Utils.getProgressBarStyle(this.getTestcasesCountByStatus(launch, 'BROKEN'), launch.testcases.length)}></div>
            </div>
        )
    }

    getTestcasesCountByStatus(launch, status){
        return launch.testcases.filter(testcase => testcase.status == status).length;
    }

    handlePageChanged(newPage) {
        this.state.pager.current = newPage;
        this.state.filter.page = newPage;
        this.getLaunches(this);
        this.setState(this.state);
        this.updateUrl();
    }

    updateUrl(){
        this.props.history.push('/launches?' + Utils.filterToQuery(this.state.filter));
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
                  <table className="table table-striped">
                        <thead>
                            <tr>
                              <th scope="col">Title</th>
                              <th scope="col">Progress</th>
                              <th scope="col">Created</th>
                              <th scope="col">Started</th>
                              <th scope="col">Finished</th>
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
                                         </tr>
                                         );
                              }.bind(this))
                        }
                        </tbody>
                    </table>
                    <div className="pager">
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
