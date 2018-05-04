import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TripAward } from './trip-award.model';
import { TripAwardService } from './trip-award.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-trip-award',
    templateUrl: './trip-award.component.html'
})
export class TripAwardComponent implements OnInit, OnDestroy {
tripAwards: TripAward[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private tripAwardService: TripAwardService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.tripAwardService.query().subscribe(
            (res: HttpResponse<TripAward[]>) => {
                this.tripAwards = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTripAwards();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TripAward) {
        return item.id;
    }
    registerChangeInTripAwards() {
        this.eventSubscriber = this.eventManager.subscribe('tripAwardListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
