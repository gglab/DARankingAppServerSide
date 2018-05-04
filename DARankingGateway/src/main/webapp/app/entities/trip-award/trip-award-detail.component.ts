import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TripAward } from './trip-award.model';
import { TripAwardService } from './trip-award.service';

@Component({
    selector: 'jhi-trip-award-detail',
    templateUrl: './trip-award-detail.component.html'
})
export class TripAwardDetailComponent implements OnInit, OnDestroy {

    tripAward: TripAward;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private tripAwardService: TripAwardService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTripAwards();
    }

    load(id) {
        this.tripAwardService.find(id)
            .subscribe((tripAwardResponse: HttpResponse<TripAward>) => {
                this.tripAward = tripAwardResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTripAwards() {
        this.eventSubscriber = this.eventManager.subscribe(
            'tripAwardListModification',
            (response) => this.load(this.tripAward.id)
        );
    }
}
