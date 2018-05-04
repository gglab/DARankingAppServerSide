import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TripAward } from './trip-award.model';
import { TripAwardPopupService } from './trip-award-popup.service';
import { TripAwardService } from './trip-award.service';

@Component({
    selector: 'jhi-trip-award-dialog',
    templateUrl: './trip-award-dialog.component.html'
})
export class TripAwardDialogComponent implements OnInit {

    tripAward: TripAward;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private tripAwardService: TripAwardService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.tripAward.id !== undefined) {
            this.subscribeToSaveResponse(
                this.tripAwardService.update(this.tripAward));
        } else {
            this.subscribeToSaveResponse(
                this.tripAwardService.create(this.tripAward));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TripAward>>) {
        result.subscribe((res: HttpResponse<TripAward>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TripAward) {
        this.eventManager.broadcast({ name: 'tripAwardListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-trip-award-popup',
    template: ''
})
export class TripAwardPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private tripAwardPopupService: TripAwardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.tripAwardPopupService
                    .open(TripAwardDialogComponent as Component, params['id']);
            } else {
                this.tripAwardPopupService
                    .open(TripAwardDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
