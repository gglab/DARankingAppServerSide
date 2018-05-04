import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Trip } from './trip.model';
import { TripService } from './trip.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-trip',
    templateUrl: './trip.component.html'
})
export class TripComponent implements OnInit, OnDestroy {
trips: Trip[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private tripService: TripService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
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
            this.currentAccount = account;
        });
        this.registerChangeInTrips();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Trip) {
        return item.id;
    }
    registerChangeInTrips() {
        this.eventSubscriber = this.eventManager.subscribe('tripListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
