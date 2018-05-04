import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { DriverAward } from './driver-award.model';
import { DriverAwardService } from './driver-award.service';

@Component({
    selector: 'jhi-driver-award-detail',
    templateUrl: './driver-award-detail.component.html'
})
export class DriverAwardDetailComponent implements OnInit, OnDestroy {

    driverAward: DriverAward;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private driverAwardService: DriverAwardService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDriverAwards();
    }

    load(id) {
        this.driverAwardService.find(id)
            .subscribe((driverAwardResponse: HttpResponse<DriverAward>) => {
                this.driverAward = driverAwardResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDriverAwards() {
        this.eventSubscriber = this.eventManager.subscribe(
            'driverAwardListModification',
            (response) => this.load(this.driverAward.id)
        );
    }
}
