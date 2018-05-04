import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Driver } from '../entities/driver/driver.model';
import { DriverService } from '../entities/driver/driver.service';
import { Trip } from '../entities/trip/trip.model';
import { TripService } from '../entities/trip/trip.service';
import { Account, LoginModalService, Principal } from '../shared';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.css'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    drivers: Driver[];
    trips: Trip[];
    eventSubscriber: Subscription;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private driverService: DriverService,
        private tripService: TripService,
        private jhiAlertService: JhiAlertService,
    ) {
    }

    loadAll() {
                this.driverService.query().subscribe(
                    (res: HttpResponse<Driver[]>) => {
                        this.drivers = res.body;
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
                this.tripService.query().subscribe(
                                        (res: HttpResponse<Trip[]>) => {
                                            this.trips = res.body;
                                        },
                                        (res: HttpErrorResponse) => this.onError(res.message)
                                    );
               }

    ngOnInit() {
    this.loadAll();
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.registerChangeInDrivers();
        this.registerChangeInTrips();
    }

    private onError(error) {
                this.jhiAlertService.error(error.message, null, null);
            }

    registerChangeInDrivers() {
                this.eventSubscriber = this.eventManager.subscribe('driverListModification', (response) => this.loadAll());
            }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    trackDriverRank(index: number, item: Driver) {
                return item.rank;
            }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();

    }

    trackTripDate(index: number, item: Trip) {
                    return item.start;
                }

    registerChangeInTrips() {
                    this.eventSubscriber = this.eventManager.subscribe('tripListModification', (response) => this.loadAll());
                }
}
