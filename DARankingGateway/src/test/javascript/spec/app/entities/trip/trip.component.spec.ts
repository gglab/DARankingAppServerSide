/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { DaRankingGatewayTestModule } from '../../../test.module';
import { TripComponent } from '../../../../../../main/webapp/app/entities/trip/trip.component';
import { TripService } from '../../../../../../main/webapp/app/entities/trip/trip.service';
import { Trip } from '../../../../../../main/webapp/app/entities/trip/trip.model';

describe('Component Tests', () => {

    describe('Trip Management Component', () => {
        let comp: TripComponent;
        let fixture: ComponentFixture<TripComponent>;
        let service: TripService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [DaRankingGatewayTestModule],
                declarations: [TripComponent],
                providers: [
                    TripService
                ]
            })
            .overrideTemplate(TripComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TripComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TripService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Trip(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.trips[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
