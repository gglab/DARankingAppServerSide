/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { DaRankingGatewayTestModule } from '../../../test.module';
import { TripDetailComponent } from '../../../../../../main/webapp/app/entities/trip/trip-detail.component';
import { TripService } from '../../../../../../main/webapp/app/entities/trip/trip.service';
import { Trip } from '../../../../../../main/webapp/app/entities/trip/trip.model';

describe('Component Tests', () => {

    describe('Trip Management Detail Component', () => {
        let comp: TripDetailComponent;
        let fixture: ComponentFixture<TripDetailComponent>;
        let service: TripService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DaRankingGatewayTestModule],
                declarations: [TripDetailComponent],
                providers: [
                    TripService
                ]
            })
            .overrideTemplate(TripDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TripDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TripService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Trip(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.trip).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
